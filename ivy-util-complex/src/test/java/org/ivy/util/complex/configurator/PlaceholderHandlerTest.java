package org.ivy.util.complex.configurator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * <p>
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @description TODO
 * @date 2020/5/9 11:40
 */
@Slf4j
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class PlaceholderHandlerTest {

    private static final PlaceholderHandler HANDLER = PlaceholderHandler.newInstance();

    @BeforeAll
    public static void setUp() {
        log.debug("===={msg: {}}", "setup");
    }
    @Test
    public void test_00_warmUp() {
        String[] messages = {
//                "hello #{0}，please #{1}， you are #{2}",
//                "hello #{0}，please #{1}， you are #{}",
//                "hello #{0}，please #{10}， you are #{2}",
                "hello #{ 0 }，please #{1 }， you are #{028 }, #{}",
        };
        String message;
        for (String e : messages) {
            message = HANDLER.handle(e, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
            log.info("{key: {}, val: {}}", e, message);
        }
    }
    @Test
    public void test_01_op_tr() {

    }
    @Test
    public void test_02_op_tps() {

    }


}
