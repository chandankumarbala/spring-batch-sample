package com.batch.job;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.batch.item.ItemPojo;
import com.batch.processor.CustomProcesor;

@Configuration
@EnableBatchProcessing
public class BatchJobConfig {

	@Autowired
	private DataSource datasource;

	@Autowired
	private JobBuilderFactory jobMaker;

	@Autowired
	private StepBuilderFactory stepMaker;

	@Bean("faltfilereade")
	public FlatFileItemReader<ItemPojo> reader() {
		/*
		 * return new FlatFileItemReaderBuilder<ItemPojo>() .name("pojoItemReader")
		 * .resource(new ClassPathResource("employees.csv")) .delimited() .names(new
		 * String[]{"firstName", "lastName"}) .fieldSetMapper(new
		 * BeanWrapperFieldSetMapper<ItemPojo>() {{ setTargetType(ItemPojo.class); }})
		 * .build();
		 */

		FlatFileItemReader<ItemPojo> reader = new FlatFileItemReader<ItemPojo>();
		reader.setResource(new ClassPathResource("employees.csv"));
		reader.setLinesToSkip(1);
		reader.setLineMapper(new DefaultLineMapper<ItemPojo>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "firstName", "lastName", "friend1", "friend2", "friend3" });
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<ItemPojo>() {
					{
						setTargetType(ItemPojo.class);
					}
				});
			}
		});
		return reader;
	}

	@Bean("passProcessor")
	public ItemProcessor<ItemPojo, ItemPojo> processor() {
		return new CustomProcesor();
	}

	@Bean("jdbcWriter1")
	public ItemWriter<ItemPojo> writer(DataSource datasource) {
		JdbcBatchItemWriter<ItemPojo> itemWriter = new JdbcBatchItemWriter<ItemPojo>();
		itemWriter.setDataSource(datasource);
		itemWriter.setSql(
				"INSERT INTO employee(firstName, lastName, relation) VALUES (:firstName, :lastName, concat(:firstName, :lastName)) ON CONFLICT (relation) DO NOTHING");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<ItemPojo>());
		itemWriter.setAssertUpdates(false);
		return itemWriter;
	}

	@Bean("jdbcWriter2")
	public ItemWriter<ItemPojo> writer1(DataSource datasource) {
		JdbcBatchItemWriter<ItemPojo> itemWriter = new JdbcBatchItemWriter<ItemPojo>();
		itemWriter.setDataSource(datasource);
		itemWriter.setSql(
				"INSERT INTO friend(relation,friend) VALUES (concat(:firstName, :lastName),:friend1),(concat(:firstName, :lastName),:friend2),(concat(:firstName, :lastName),:friend3)");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<ItemPojo>());
		return itemWriter;
	}

	@Bean("compositeWriter")
	public CompositeItemWriter<ItemPojo> compositeWriter(
			@Autowired @Qualifier("jdbcWriter1") ItemWriter<ItemPojo> writer1,
			@Autowired @Qualifier("jdbcWriter2") ItemWriter<ItemPojo> writer2) {
		CompositeItemWriter<ItemPojo> compositeItemWriter = new CompositeItemWriter<>();
		List<org.springframework.batch.item.ItemWriter<? super ItemPojo>> delegates = new ArrayList<>();
		delegates.add(writer1);
		delegates.add(writer2);
		compositeItemWriter.setDelegates(delegates);
		return compositeItemWriter;
	}

	@Bean("step1")
	public Step step(@Autowired @Qualifier("faltfilereade") FlatFileItemReader<ItemPojo> reader,
			@Autowired @Qualifier("passProcessor") ItemProcessor<ItemPojo, ItemPojo> processor,
			@Autowired @Qualifier("compositeWriter") CompositeItemWriter<ItemPojo> writer) {
		return this.stepMaker.get("step1").<ItemPojo, ItemPojo>chunk(2).faultTolerant().reader(reader)
				.processor(new CustomProcesor()).writer(writer).build();
	}

	@Bean
	public Job job(@Autowired @Qualifier("step1") Step step) {
		return jobMaker.get("readCSVFilesJob").incrementer(new RunIdIncrementer()).start(step).build();
	}
}
