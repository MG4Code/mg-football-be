package com.mg.backend.player;

import com.mg.backend.player.data.dto.Player;
import com.mg.backend.player.data.repo.PlayerRepository;
import io.reactivex.Flowable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PlayerControllerTest {

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private PlayerRepository playerRepository;

  private MockMvc mvc;

  @Before
  public void setup() {
    mvc = MockMvcBuilders
      .webAppContextSetup(context)
      .apply(SecurityMockMvcConfigurers.springSecurity())
      .build();
  }

  @Test
  @WithMockUser(username = "georg", roles = {"BAR"})
  public void listAllWillReturnOnePlayer() throws Exception {
    when(playerRepository.findAll()).thenReturn(Flowable.just(new Player().setFirstName("foobar")));
    var mvcResult = mvc.perform(get("/player"))
      .andExpect(request().asyncStarted())
//      .andDo(MockMvcResultHandlers.log())
      .andReturn();

    mvc.perform(asyncDispatch(mvcResult))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith("application/json"))
      .andExpect(jsonPath("$").isArray())
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0].firstName", is("foobar")))

    ;

  }

  @Test
  @WithMockUser(username = "georg", roles = {"XZY"})
  public void authenticatedUserForbidden() throws Exception {
    mvc.perform(get("/player").contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden());
  }
}
