package com.akiner.brokage_firm.model;

import com.akiner.brokage_firm.rest.dto.DepositWithdrawRequest;
import com.akiner.brokage_firm.rest.dto.StockOrderDto;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DtoTest {

    public void testDto(Class<?> dtoClass) throws Exception {
        Object dtoInstance = dtoClass.getDeclaredConstructor().newInstance();

        for (Field field : dtoClass.getDeclaredFields()) {
            String fieldName = field.getName();
            String setterName =
                    "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            String getterName =
                    "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

            Method setter =
                    Arrays.stream(dtoClass.getDeclaredMethods())
                            .filter(method -> method.getName().equals(setterName))
                            .findFirst()
                            .orElse(null);
            if (setter == null) {
                continue;
            }

            Method getter =
                    Arrays.stream(dtoClass.getDeclaredMethods())
                            .filter(method -> method.getName().equals(getterName))
                            .findFirst()
                            .orElseThrow(
                                    () ->
                                            new Exception(
                                                    "Getter not found for field: " + fieldName));

            Object value = getTestValue(field.getType());
            setter.invoke(dtoInstance, value);

            assertEquals(value, getter.invoke(dtoInstance));
        }
    }

    private Object getTestValue(Class<?> type) {
        if (type == int.class || type == Integer.class) {
            return 1;
        } else if (type == long.class || type == Long.class) {
            return 1L;
        } else if (type == double.class || type == Double.class) {
            return 1.0;
        } else if (type == boolean.class || type == Boolean.class) {
            return true;
        } else if (type == String.class) {
            return "test";
        } else {
            return null;
        }
    }

    @Test
    public void testCustomerDto() throws Exception {
        DtoTest dtoTester = new DtoTest();
        dtoTester.testDto(Asset.class);
        dtoTester.testDto(Customer.class);
        //        dtoTester.testDto(OrderSide.class);
        //        dtoTester.testDto(OrderStatus.class);
        dtoTester.testDto(StockOrder.class);
        dtoTester.testDto(Transaction.class);
        //        dtoTester.testDto(TransactionOperation.class);
        dtoTester.testDto(DepositWithdrawRequest.class);
        dtoTester.testDto(StockOrderDto.class);
    }
}
