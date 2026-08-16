package com.example.JMSCommerce.Utility.enums;

import java.util.EnumSet;
import java.util.Set;

public enum OrderStatus {

    // Define allowed next states for each status
    DELIVERED, // Terminal state (no further updates allowed)

    SHIPPED {
        @Override
        public Set<OrderStatus> nextStates() {
            return EnumSet.of(DELIVERED, CANCELLED);
        }
    },

    PROCESSING {
        @Override
        public Set<OrderStatus> nextStates() {
            return EnumSet.of(SHIPPED, CANCELLED);
        }
    },

    CONFIRMED {
        @Override
        public Set<OrderStatus> nextStates() {
            return EnumSet.of(PROCESSING, CANCELLED);
        }
    },

    PENDING {
        @Override
        public Set<OrderStatus> nextStates() {
            return EnumSet.of(CONFIRMED, CANCELLED);
        }
    },

    CANCELLED; // Terminal status

    // Default implementation returns an empty set (terminal state)
    public Set<OrderStatus> nextStates() {
        return EnumSet.noneOf(OrderStatus.class);
    }

    // Validation helper
    public boolean canTransitionTo(OrderStatus targetStatus) {
        return nextStates().contains(targetStatus);
    }
}
