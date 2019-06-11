package test.ideahut.sbms.sample.api.proxy;

import org.junit.Test;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.ideahut.sbms.sample.client.service.TestService;

public class HttpInvokerTestServiceTest {

	private TestService setUp() {
		HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
		proxy.setServiceInterface(TestService.class);
		proxy.setServiceUrl("http://localhost:8086/httpinvoker/TestService");
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
		System.out.println(service.test(null));
		time = System.currentTimeMillis() - time;
		System.out.println(">>> Process Time: " + time + " ms");
	}
	
	
	
}
