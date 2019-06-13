package test.ideahut.sbms.sample.api.proxy;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.github.ideahut.sbms.client.service.ServiceProxyHeader;
import com.github.ideahut.sbms.client.service.ServiceProxyWrapper;
import com.github.ideahut.sbms.httpinvoker.proxy.HttpInvokerProxyFactoryBean;
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
		ServiceProxyHeader.set("__KEY__", System.nanoTime() + "");
		long time = System.currentTimeMillis();
		System.out.println(service.halo());
		System.out.println(service.add(1, 3));
		System.out.println(service.code());
		System.out.println(service.text("XXXX"));
		System.out.println(service.test(null));
		time = System.currentTimeMillis() - time;
		System.out.println(">>> Process Time: " + time + " ms");
	}
	
	@Test
	public void wrapper() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("__KEY__", System.nanoTime() + "");
		TestService service = ServiceProxyWrapper.withHeaders(TestService.class, setUp(), headers);
		long time = System.currentTimeMillis();
		System.out.println(service.halo());
		System.out.println(service.add(1, 3));
		System.out.println(service.code());
		ServiceProxyHeader.set("__XXX__", System.nanoTime() + "");
		System.out.println(service.text("XXXX"));
		System.out.println(service.test(null));
		time = System.currentTimeMillis() - time;
		System.out.println(">>> Process Time: " + time + " ms");
	}
	
	
	
}
