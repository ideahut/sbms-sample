package test.ideahut.sbms.sample.api.proxy;

import java.math.BigDecimal;

import org.junit.Test;

import com.github.ideahut.sbms.client.dto.CodeMessageDto;
import com.github.ideahut.sbms.client.dto.ResponseDto;
import com.github.ideahut.sbms.client.exception.ResponseException;
import com.github.ideahut.sbms.client.remote.RemoteMethodHandler;
import com.github.ideahut.sbms.client.remote.RemoteMethodParameter;
import com.github.ideahut.sbms.client.remote.RemoteMethodService;
import com.github.ideahut.sbms.hessian.proxy.HessianProxyFactoryBean;
import com.ideahut.sbms.sample.client.dto.TestDto;
import com.ideahut.sbms.sample.client.service.TestService;

public class HessianRemoteMethodServiceTest {
	
	private RemoteMethodService setUp() {
		HessianProxyFactoryBean proxy = new HessianProxyFactoryBean();
		proxy.setServiceInterface(RemoteMethodService.class);
		proxy.setServiceUrl("http://localhost:8086/hessian/RemoteMethodService");
		proxy.afterPropertiesSet();
		return (RemoteMethodService)proxy.getObject();
	}
	
	@Test
	public void raw() {
		System.out.println(">>> RAW");
		try {
			RemoteMethodService service = setUp();
			System.out.println(service.call(String.class, new RemoteMethodParameter(TestService.class, "halo")));
			System.out.println(service.call(int.class, new RemoteMethodParameter(TestService.class, "add", 1, 3)));
			System.out.println(service.call(CodeMessageDto.class, new RemoteMethodParameter(TestService.class, "code")));
			System.out.println(service.call(ResponseDto.class, new RemoteMethodParameter(TestService.class, "text", "XXXX")));
			service.call(null, new RemoteMethodParameter(TestService.class, "poke", true));
			System.out.println(service.call(TestDto.class, new RemoteMethodParameter(TestService.class, "test", new BigDecimal(200000.50))));
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
			System.out.println(service.test(new BigDecimal(200000.50)));
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
