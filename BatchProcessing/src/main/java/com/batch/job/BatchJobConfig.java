package com.batch.job;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.batch.item.ItemPojo;

@Configuration
@EnableBatchProcessing
public class BatchJobConfig {

	@Autowired
	private JobBuilderFactory jobMaker;
	
	
	@Autowired
	private StepBuilderFactory stepMaker;
	
	@Bean
    public FlatFileItemReader<ItemPojo> reader() {
        return new FlatFileItemReaderBuilder<ItemPojo>()
            .name("pojoItemReader")
            .resource(new ClassPathResource("employees.csv"))
            .delimited()
            .names(new String[]{"firstName", "lastName"})
            .fieldSetMapper(new BeanWrapperFieldSetMapper<ItemPojo>() {{
                setTargetType(ItemPojo.class);
            }})
            .build();
    }

	
	
	
	
}
