package com.devsuperior.bds03.dto;

import com.devsuperior.bds03.services.validation.UserInsertValid;

import javax.validation.constraints.Size;

@UserInsertValid
public class UserInsertDTO extends UserDTO{
    private static final long serialVersionUID=1L;
    @Size(min = 6, max = 12,message = "A senha tede ter entre 6 e 12 caracteres")
    private String password;

    private UserInsertDTO(){
        super();
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
