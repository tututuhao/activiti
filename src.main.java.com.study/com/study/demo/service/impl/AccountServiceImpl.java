package com.study.demo.service.impl;

import com.study.demo.entity.Account;
import com.study.demo.mapper.AccountMapper;
import com.study.demo.service.IAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账户 服务实现类
 * </p>
 *
 * @author tuhao
 * @since 2024-06-25
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

}
