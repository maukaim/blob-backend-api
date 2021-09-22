package com.maukaim.cryptohub.web.stream;

public interface DataDispatcher<D> {
    void addListener(String extUuid, D dispatcher);
}
