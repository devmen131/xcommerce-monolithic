package ma.aui.sse.it.xcommerce.monolithic;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class XCommerceMonolithicApplicationTests extends AbstractTestIT {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void contextLoads() throws Exception {
		mockMvc.perform(get("/rest/order/list")).andExpect(status().isOk());
	}

}
