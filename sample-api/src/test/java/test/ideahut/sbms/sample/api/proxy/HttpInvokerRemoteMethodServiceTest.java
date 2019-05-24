package test.ideahut.sbms.sample.api.proxy;

import org.junit.Test;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.github.ideahut.sbms.client.dto.CodeMessageDto;
import com.github.ideahut.sbms.client.dto.RemoteMethodDto;
import com.github.ideahut.sbms.client.dto.ResponseDto;
import com.github.ideahut.sbms.client.exception.ResponseException;
import com.github.ideahut.sbms.client.handler.RemoteMethodHandler;
import com.github.ideahut.sbms.client.service.RemoteMethodService;
import com.ideahut.sbms.sample.client.service.TestService;

public class HttpInvokerRemoteMethodServiceTest {

	private RemoteMethodService setUp() {
		HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
		proxy.setServiceInterface(RemoteMethodService.class);
		proxy.setServiceUrl("http://localhost:8086/httpinvoker/RemoteMethodService");
		proxy.afterPropertiesSet();
		return (RemoteMethodService)proxy.getObject();
	}
	
	@Test
	public void raw() {
		System.out.println(">>> RAW");
		try {
			RemoteMethodService service = setUp();
			System.out.println(service.call(String.class, new RemoteMethodDto(TestService.class, "halo")));
			System.out.println(service.call(int.class, new RemoteMethodDto(TestService.class, "add", 1, 3)));
			System.out.println(service.call(CodeMessageDto.class, new RemoteMethodDto(TestService.class, "code")));
			System.out.println(service.call(ResponseDto.class, new RemoteMethodDto(TestService.class, "text", "XXXX")));
			service.call(null, new RemoteMethodDto(TestService.class, "poke", true));
		} catch (ResponseException e) {
			CodeMessageDto error = e.getResponse().getError().get(0);
			System.err.println(error.getCode() + ": " + error.getMessage());
		}
		System.out.println("<<<");
	}
	
	@Test
	public void handler() {
		System.out.println(">>> HANDLER");
		try {
			RemoteMethodService remoteMethodService = setUp();
			TestService service = RemoteMethodHandler.create(TestService.class, remoteMethodService);
			System.out.println(service.halo());
			System.out.println(service.add(1, 3));
			System.out.println(service.code());
			System.out.println(service.text("XXXX"));
			service.poke(true);
		} catch (Exception e) {
			if (e.getCause() instanceof ResponseException) {
				CodeMessageDto error = ((ResponseException)e.getCause()).getResponse().getError().get(0);
				System.err.println(error.getCode() + ": " + error.getMessage());
			} else {
				e.printStackTrace();
			}
		}
		System.out.println("<<<");
	}
	
}
