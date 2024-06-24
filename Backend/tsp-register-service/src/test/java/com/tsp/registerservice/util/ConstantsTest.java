package com.tsp.registerservice.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConstantsTest {

    @Test
    void test_constants(){
        assertNotNull(Constants.FORGOT_PASSWORD_SUCCESS_MSG);
        assertNotNull(Constants.DELETE_SUCCESS_MSG);
        assertNotNull(Constants.RESET_PASSWORD_SUCCESS_MSG);
    }
}