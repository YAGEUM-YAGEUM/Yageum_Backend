package com.yageum.fintech.global.model.Result;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListResult<T> extends CommonResult{
    private List<T> list;//모든 타입이 다들어감.
}