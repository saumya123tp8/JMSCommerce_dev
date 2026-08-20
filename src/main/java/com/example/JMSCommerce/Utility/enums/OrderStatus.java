package com.example.JMSCommerce.Utility.enums;

import java.util.EnumSet;
import java.util.Set;
public enum OrderStatus {

    PENDING {
        @Override
        public Set<OrderStatus> nextStates() {
            return EnumSet.of(CONFIRMED, CANCELLED);
        }
    },

    CONFIRMED {
        @Override
        public Set<OrderStatus> nextStates() {
            return EnumSet.of(PROCESSING, CANCELLED);
        }
    },

    PROCESSING {
        @Override
        public Set<OrderStatus> nextStates() {
            return EnumSet.of(SHIPPED, CANCELLED);
        }
    },

    SHIPPED {
        @Override
        public Set<OrderStatus> nextStates() {
            return EnumSet.of(DELIVERED);
        }
    },

    DELIVERED,
    CANCELLED,
    PAYMENT_FAILED;

    public Set<OrderStatus> nextStates() {
        return EnumSet.noneOf(OrderStatus.class);
    }

    public boolean canTransitionTo(OrderStatus targetStatus) {
        return nextStates().contains(targetStatus);
    }
}
