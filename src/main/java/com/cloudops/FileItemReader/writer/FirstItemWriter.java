package com.cloudops.FileItemReader.writer;

import com.cloudops.FileItemReader.model.StudentCsv;
import com.cloudops.FileItemReader.model.StudentJdbc;
import com.cloudops.FileItemReader.model.StudentJson;
import com.cloudops.FileItemReader.model.StudentXml;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class FirstItemWriter implements ItemWriter<StudentJdbc> {
    @Override
    public void write(Chunk<? extends StudentJdbc> chunk) throws Exception {
        System.out.println("Inside Item Writer");
        chunk.forEach(System.out::println);
    }
}
