package cn.edu.xmu.user.model.vo;


import cn.edu.xmu.user.model.bo.Customer;
import lombok.Data;

@Data
public class StateVo {
    private Long Code;
    private String name;

    public StateVo(Customer.State state){
        Code=Long.valueOf(state.getCode());
        name=state.getDescription();
    }

}
