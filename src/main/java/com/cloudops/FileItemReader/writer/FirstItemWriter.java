package com.cloudops.FileItemReader.writer;

import com.cloudops.FileItemReader.model.StudentCsv;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class FirstItemWriter implements ItemWriter<StudentCsv> {
    @Override
    public void write(Chunk<? extends StudentCsv> chunk) throws Exception {
        System.out.println("Inside Item Writer");
        chunk.forEach(System.out::println);
    }
}
