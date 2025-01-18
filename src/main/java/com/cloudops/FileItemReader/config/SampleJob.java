package com.cloudops.FileItemReader.config;

import com.cloudops.FileItemReader.model.StudentCsv;
import com.cloudops.FileItemReader.model.StudentJdbc;
import com.cloudops.FileItemReader.model.StudentJson;
import com.cloudops.FileItemReader.model.StudentXml;
import com.cloudops.FileItemReader.processor.FirstItemProcessor;
import com.cloudops.FileItemReader.reader.FirstItemReader;
import com.cloudops.FileItemReader.writer.FirstItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.File;

@Configuration
public class SampleJob {

    @Autowired
    JobRepository jobRepository;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    FirstItemReader firstItemReader;

    @Autowired
    FirstItemProcessor firstItemProcessor;

    @Autowired
    FirstItemWriter firstItemWriter;

    @Autowired
    DataSource dataSource;

    @Bean
    public Job chunkJob(){
        return new JobBuilder("Chunk Job",jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(firstChunkStep())
                .build();
    }

    private Step firstChunkStep() {
        return new StepBuilder("First Chunk Step",jobRepository)
                .<StudentJdbc, StudentJdbc>chunk(3,platformTransactionManager)
                //.reader(flatFileItemReader())
                //.reader(jsonFileItemReader())
                //.reader(xmlStaxEventItemReader())
                .reader(jdbcJdbcCursorItemReader())
                //.processor(firstItemProcessor)
                .writer(firstItemWriter)
                .build();
    }

    private FlatFileItemReader<StudentCsv> flatFileItemReader(){
        FlatFileItemReader<StudentCsv> fileItemReader = new FlatFileItemReader<>();
        fileItemReader.setResource(new FileSystemResource(new File("D:\\Spring Projects\\Spring Batch Projects\\FileItemReader\\src\\main\\java\\input\\students.csv")));
        fileItemReader.setLineMapper(new DefaultLineMapper<StudentCsv>(){
            {
                setLineTokenizer(new DelimitedLineTokenizer(){
                    {
                        setNames("ID","FirstName","LastName","Email");
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<StudentCsv>(){
                    {
                        setTargetType(StudentCsv.class);
                    }
                });
            }
        });

        fileItemReader.setLinesToSkip(1);
        return fileItemReader;
    }

    private JsonItemReader<StudentJson> jsonFileItemReader(){
        JsonItemReader<StudentJson> jsonItemReader = new JsonItemReader<>();
        jsonItemReader.setResource(new FileSystemResource(new File("D:\\Spring Projects\\Spring Batch Projects\\FileItemReader\\src\\main\\java\\input\\students.json")));
        jsonItemReader.setJsonObjectReader(new JacksonJsonObjectReader<>(StudentJson.class));
        jsonItemReader.setMaxItemCount(9); // maximum 9 objects from the json will be read rest will be ignored
        jsonItemReader.setCurrentItemCount(2); // job will start reading from 2nd object of the json file
        return jsonItemReader;
    }

    private StaxEventItemReader<StudentXml> xmlStaxEventItemReader(){
        StaxEventItemReader<StudentXml> xmlReader = new StaxEventItemReader<>();
        xmlReader.setResource(new FileSystemResource(new File("D:\\Spring Projects\\Spring Batch Projects\\FileItemReader\\src\\main\\java\\input\\students.xml")));
        xmlReader.setFragmentRootElementName("student");

        /*Reading JSON and mapping it to java object is unmarshalling and reverse is marshalling, here we are doing unmarshalling*/
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setClassesToBeBound(StudentXml.class);
        xmlReader.setUnmarshaller(jaxb2Marshaller);
        return xmlReader;
    }

    public JdbcCursorItemReader<StudentJdbc> jdbcJdbcCursorItemReader(){
        JdbcCursorItemReader<StudentJdbc> jdbcItemReader = new JdbcCursorItemReader<>();
        jdbcItemReader.setDataSource(dataSource);
        jdbcItemReader.setSql("SELECT * FROM student");

        BeanPropertyRowMapper beanPropertyRowMapper = new BeanPropertyRowMapper();
        beanPropertyRowMapper.setMappedClass(StudentJdbc.class);
        jdbcItemReader.setRowMapper(beanPropertyRowMapper);
        jdbcItemReader.setMaxItemCount(9);
        jdbcItemReader.setCurrentItemCount(2);
        return jdbcItemReader;
    }
}
