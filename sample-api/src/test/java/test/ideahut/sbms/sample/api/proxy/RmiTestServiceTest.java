package test.ideahut.sbms.sample.api.proxy;

import org.junit.Test;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import com.ideahut.sbms.sample.client.service.TestService;

public class RmiTestServiceTest {

	private TestService setUp() {
		RmiProxyFactoryBean proxy = new RmiProxyFactoryBean();
		proxy.setServiceInterface(TestService.class);
		proxy.setServiceUrl("rmi://localhost:1699/TestService");
		proxy.afterPropertiesSet();
		return (TestService)proxy.getObject();
	}
	
	@Test
	public void main() {
		TestService service = setUp();
		long time = System.currentTimeMillis();
		System.out.println(service.halo());
		System.out.println(service.add(1, 3));
		System.out.println(service.code());
		System.out.println(service.text("XXXX"));
		time = System.currentTimeMillis() - time;
		System.out.println(">>> Process Time: " + time + " ms");
	}
	
	
	
}
