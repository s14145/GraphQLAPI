package com.graphql.service;

import com.graphql.model.Book;
import com.graphql.repository.BookRepository;
import com.graphql.service.datafetcher.AllBooksDataFetcher;
import com.graphql.service.datafetcher.BookDataFetcher;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

@Service
public class GraphQlService {

    @Autowired
    BookRepository bookRepository;

    @Value("classpath:books.graphql")
    Resource resource;

    private GraphQL graphQL;

    @Autowired
    private AllBooksDataFetcher allBooksDataFetcher;
    @Autowired
    private BookDataFetcher bookDataFetcher;

    // load schema at the application start up
    @PostConstruct
    private void loadSchema() throws IOException{

        // load data into HSQL
        loadDataIntoHSQL();

        // get the schema
        File schemaFile = resource.getFile();
        // parse schema
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaFile);
        RuntimeWiring wiring = buildRuntimeWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry,wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    private void loadDataIntoHSQL() {

        Stream.of(
                new Book("123", "Book of Clouds", "Kindle", new String[]{"Chloe Andrison", "Binod Rawal"},"Nov 2017"),
                new Book("124","3 Idiots", "Amazon", new String[] {"Chetan Bhagat"}, "Jan 2012"),
                new Book ("125","The Girl in Train", "Google", new String[] {"Stephen King"}, "Feb 2013")
        ).forEach(book -> {
            bookRepository.save(book);

        });
    }

    private RuntimeWiring buildRuntimeWiring() {

        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                            .dataFetcher("allBooks", allBooksDataFetcher)
                            .dataFetcher("book", bookDataFetcher))
                .build();
    }

    public GraphQL getGraphQL() {
        return graphQL;
    }
}
