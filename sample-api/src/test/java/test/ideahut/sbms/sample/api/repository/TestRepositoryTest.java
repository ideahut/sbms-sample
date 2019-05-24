package test.ideahut.sbms.sample.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.ideahut.sbms.sample.api.repository.TestRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TestRepositoryTest {
	
	//@Autowired 
	//private DataSource dataSource;
	  
	//@Autowired 
	//private JdbcTemplate jdbcTemplate;
	  
	@Autowired 
	private TestEntityManager entityManager;
	  
	@Autowired 
	private TestRepository testRepository;
	
	
	@Test
	public void injectedComponentsAreNotNull(){
		//assertThat(dataSource).isNotNull();
		//assertThat(jdbcTemplate).isNotNull();
		assertThat(entityManager).isNotNull();
		assertThat(testRepository).isNotNull();
	}
	
}
