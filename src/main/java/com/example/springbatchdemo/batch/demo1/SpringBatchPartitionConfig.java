package com.example.springbatchdemo.batch.demo1;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;

@Configuration
@EnableBatchProcessing
public class SpringBatchPartitionConfig {

    private final JobBuilderFactory jobs;
    private final StepBuilderFactory steps;

    @Autowired
    public SpringBatchPartitionConfig(JobBuilderFactory jobs, StepBuilderFactory steps) {
        this.jobs = jobs;
        this.steps = steps;
    }

    @Bean(name = "partitionerJob")
    public Job partitionerJob() throws Exception {
        return jobs.get("partitionerJob")
                .start(partitionStep())
                .build();
    }

    @Bean
    public Step partitionStep() throws Exception {
        return steps.get("partitionStep")
                .partitioner("slaveStep", partitioner())
                .gridSize(5)
                .step(slaveStep())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step slaveStep() throws Exception {
        return steps.get("slaveStep")
                .chunk(1)
                .reader(itemReader(null))
                .processor(new CSVItemProcessor())
                .writer(itemWriter(null))
                .listener(new CSVItemWriterListener<>())
                .build();
    }

    @Bean
    public CSVResourcePartitioner partitioner() {
        CSVResourcePartitioner partitioner = new CSVResourcePartitioner();
        Resource[] resources;
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            resources = resolver.getResources("classpath*:input/*.csv");
        } catch (IOException e) {
            throw new RuntimeException("I/O problems when resolving the input file pattern.", e);
        }
        partitioner.setResources(resources);
        return partitioner;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Employee> itemReader(@Value("#{stepExecutionContext[inputFileName]}") String filename) throws Exception {
        //Create reader instance
        FlatFileItemReader<Employee> reader = new FlatFileItemReader<>();

        //Set input file location
        reader.setResource(new FileSystemResource("src/main/resources/input/" + filename));

        //Set number of lines to skips. Use it if file has header rows.
        reader.setLinesToSkip(1);

        //Configure how each line will be parsed and mapped to different values
        reader.setLineMapper(new DefaultLineMapper() {
            {
                //3 columns in each row
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[] { "id", "firstName", "lastName" });
                    }
                });
                //Set values in Employee class
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {
                    {
                        setTargetType(Employee.class);
                    }
                });
            }
        });
        return reader;
    }

    @Bean(destroyMethod = "")
    @StepScope
    public CSVItemWriter itemWriter(@Value("#{stepExecutionContext[outputFileName]}") String filename) throws Exception {
        CSVItemWriter itemWriter = new CSVItemWriter();
        itemWriter.setResource(new FileSystemResource("src/main/resources/output/" + filename));
        return itemWriter;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(3);
        taskExecutor.setCorePoolSize(3);
        taskExecutor.setQueueCapacity(5);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }
}
