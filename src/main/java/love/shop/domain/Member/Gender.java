package love.shop.domain.Member;

public enum Gender {
    MAN("남자"), WOMEN("여자");

     private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return gender;
    }
}
