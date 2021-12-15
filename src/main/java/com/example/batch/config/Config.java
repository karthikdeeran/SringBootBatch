package com.example.batch.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.example.batch.dao.Employee;
import com.example.batch.service.EmployeeFieldMapper;
import com.example.batch.service.EmployeeProcessor;

@Configuration
@EnableBatchProcessing
public class Config {
    private final Logger logger = LoggerFactory.getLogger(Config.class);

	   @Autowired
	    public JobBuilderFactory jobBuilderFactory;

	    @Autowired
	    public StepBuilderFactory stepBuilderFactory;
	    
	    @Autowired
	    private ItemWriter<Employee> writer;
	    
	    public ThreadPoolTaskExecutor taskExecutor() {
		    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
	        taskExecutor.setCorePoolSize(5);
	        taskExecutor.setMaxPoolSize(10);
	        taskExecutor.setQueueCapacity(10);
	        taskExecutor.afterPropertiesSet();
	        return taskExecutor;
	    }
	    
	    @Bean
		public TaskExecutor ayncTaskExecutor(){
		    SimpleAsyncTaskExecutor asyncTaskExecutor=new SimpleAsyncTaskExecutor("spring_batch");
		    asyncTaskExecutor.setConcurrencyLimit(5);
		    return asyncTaskExecutor;
		}

	    @Bean
	    public Job job() {
	        return jobBuilderFactory .get("BatchJob")
	                .start(masterStep())
	                .build();
	    }
	    
	    @Bean
	    @Qualifier("masterStep")
	    protected Step masterStep() {
	        return stepBuilderFactory.get("masterStep")
	                . partitioner("masterStep", partitioner())
	                .step(step1())
	                .build();
	    }
	    
	    @Bean("partitioner")
	    @StepScope
	    protected Partitioner partitioner() {
	        MultiResourcePartitioner partitioner = new  MultiResourcePartitioner();
	        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	        Resource[]  resource = null;
	        try {
	        	resource = 	resolver.getResources("file:src/main/resources/*.csv");
		  		logger.debug("#partitioner#"+resource);

	        }catch (Exception e) {
				 e.printStackTrace();
			}
	        partitioner.setResources(resource);
	        partitioner.partition(10);
	        return partitioner;
	    }

	    @Bean
	    protected Step step1() {
	        return stepBuilderFactory.get("step1")
	                .<Employee,Employee>chunk(1000)
	                 .processor(processor())
	                . writer(writer)
	                .reader(reader(null))
	                .taskExecutor(taskExecutor())
	                .build();
	    }
	    
	  @Bean
	  public EmployeeProcessor processor() {
		  return new EmployeeProcessor();
	  }
	  
	  @Bean
	  @StepScope
	    public FlatFileItemReader<Employee> reader(@Value("#{stepExecutionContext[fileName]}") String filename) {
		  logger.debug("#filename#"+filename);
		  logger.debug("#filename#"+filename.substring(6));
		  
	        return new FlatFileItemReaderBuilder<Employee>() 
	                .name("Reader")
	                .resource(new ClassPathResource("Employee.csv"))
	                .delimited()
	                .names(new String[]{"emp_name", "emp_age"})
	                .lineMapper(lineMapper())
	                .fieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {{
	                    setTargetType(Employee.class);
	                }})
	                .build();
	    }
	  
	  @Bean
	    public LineMapper<Employee> lineMapper() {
	        final DefaultLineMapper<Employee> defaultLineMapper = new DefaultLineMapper<>();
	        final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
	        lineTokenizer.setDelimiter(",");
	        lineTokenizer.setStrict(false);
	        lineTokenizer.setNames(new String[] {"emp_name","emp_age"});
	        final EmployeeFieldMapper fieldSetMapper = new EmployeeFieldMapper();
	        defaultLineMapper.setLineTokenizer(lineTokenizer);
	        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
	        return defaultLineMapper;
	    }
	 
}
