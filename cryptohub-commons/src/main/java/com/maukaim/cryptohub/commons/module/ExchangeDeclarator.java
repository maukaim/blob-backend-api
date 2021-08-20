package com.maukaim.cryptohub.commons.module;

import com.maukaim.cryptohub.commons.exchanges.ExchangeService;
import com.maukaim.cryptohub.commons.exchanges.model.ConnectionParameter;
import com.maukaim.cryptohub.commons.module.ModuleDeclarator;

import java.util.List;
import java.util.Map;

public interface ExchangeDeclarator extends ModuleDeclarator {
    Map<String,List<ConnectionParameter>> getConnectionParameters();

    ExchangeService getInstance();
    void activate(List caca);
}
