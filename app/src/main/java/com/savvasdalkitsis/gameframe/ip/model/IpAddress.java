package com.savvasdalkitsis.gameframe.ip.model;

import static com.savvasdalkitsis.gameframe.ip.model.IpAddress.Builder.ipAddress;

public class IpAddress {

    private String part1;
    private String part2;
    private String part3;
    private String part4;

    public static IpAddress parse(String string) {
        String[] parts = string.split("\\.");
        return ipAddress()
                .part1(parts[0])
                .part2(parts[1])
                .part3(parts[2])
                .part4(parts[3])
                .build();
    }

    private IpAddress(Builder builder) {
        part1 = builder.part1;
        part2 = builder.part2;
        part3 = builder.part3;
        part4 = builder.part4;
    }

    public String getPart1() {
        return part1;
    }

    public String getPart2() {
        return part2;
    }

    public String getPart3() {
        return part3;
    }

    public String getPart4() {
        return part4;
    }

    @Override
    public String toString() {
        return String.format("%s.%s.%s.%s", part1, part2, part3, part4);
    }

    public boolean isValid() {
        return isPartValid(part1)
                && isPartValid(part2)
                && isPartValid(part3)
                && isPartValid(part4);
    }

    private boolean isPartValid(String part) {
        int number;
        try {
            number = Integer.parseInt(part);
        } catch (Exception e) {
            number = -1;
        }
        return number >= 0 && number <= 255;
    }

    public static final class Builder {
        private String part1;
        private String part2;
        private String part3;
        private String part4;

        public static Builder ipAddress() {
            return new Builder();
        }

        private Builder() {
        }

        public Builder part1(String part1) {
            this.part1 = part1;
            return this;
        }

        public Builder part2(String part2) {
            this.part2 = part2;
            return this;
        }

        public Builder part3(String part3) {
            this.part3 = part3;
            return this;
        }

        public Builder part4(String part4) {
            this.part4 = part4;
            return this;
        }

        public IpAddress build() {
            return new IpAddress(this);
        }
    }
}
