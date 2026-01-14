package com.mpai.mvp.presenter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePresenter {

  @GetMapping({"/", ""})
  public String home() {
    return "home";
  }
}
