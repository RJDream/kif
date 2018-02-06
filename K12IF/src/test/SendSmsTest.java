package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.aplus.utils.SendSms;

public class SendSmsTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		String[] phone = new String[]{"18877788873"};
		String result = SendSms.sendMessage("你有一项编号为 12345 的事务需要处理", phone);
		System.out.println(result);
		assertEquals("1", 1);
	}

}
