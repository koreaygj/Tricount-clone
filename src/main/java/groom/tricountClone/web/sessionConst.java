package groom.tricountClone.web;

import lombok.Getter;

public enum sessionConst {
  LOGIN_MEMBER("loginMember");

  @Getter
  String name;

  sessionConst(String name) {
    this.name = name;
  }
}
