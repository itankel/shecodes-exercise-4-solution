package com.ifat.example.useawssdk.withdynamodb.shecodesexercise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;

@DynamoDbBean
@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
public class DogItem {
    private String breed;
    private String breedType;
    private String origin;
    private String popularity;
    private List<String> temperament;
    private String hypoallergenic;
    private int intelligence;
    private String photo;


    public String getBreed() {
        return this.breed;
    }

    public String getBreedType() {
        return this.breedType;
    }

    public String getOrigin() {
        return this.origin;
    }

    public String getPopularity() {
        return this.popularity;
    }

    public List<String> getTemperament() {
        return this.temperament;
    }

    public String getHypoallergenic() {
        return this.hypoallergenic;
    }

    public int getIntelligence() {
        return this.intelligence;
    }

    public String getPhoto() {
        return this.photo;
    }

    @DynamoDbPartitionKey
    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setBreedType(String breedType) {
        this.breedType = breedType;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public void setTemperament(List<String> temperament) {
        this.temperament = temperament;
    }

    public void setHypoallergenic(String hypoallergenic) {
        this.hypoallergenic = hypoallergenic;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof DogItem)) return false;
        final DogItem other = (DogItem) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$breed = this.getBreed();
        final Object other$breed = other.getBreed();
        if (this$breed == null ? other$breed != null : !this$breed.equals(other$breed)) return false;
        final Object this$breedType = this.getBreedType();
        final Object other$breedType = other.getBreedType();
        if (this$breedType == null ? other$breedType != null : !this$breedType.equals(other$breedType)) return false;
        final Object this$origin = this.getOrigin();
        final Object other$origin = other.getOrigin();
        if (this$origin == null ? other$origin != null : !this$origin.equals(other$origin)) return false;
        final Object this$popularity = this.getPopularity();
        final Object other$popularity = other.getPopularity();
        if (this$popularity == null ? other$popularity != null : !this$popularity.equals(other$popularity))
            return false;
        final Object this$temperament = this.getTemperament();
        final Object other$temperament = other.getTemperament();
        if (this$temperament == null ? other$temperament != null : !this$temperament.equals(other$temperament))
            return false;
        final Object this$hypoallergenic = this.getHypoallergenic();
        final Object other$hypoallergenic = other.getHypoallergenic();
        if (this$hypoallergenic == null ? other$hypoallergenic != null : !this$hypoallergenic.equals(other$hypoallergenic))
            return false;
        if (this.getIntelligence() != other.getIntelligence()) return false;
        final Object this$photo = this.getPhoto();
        final Object other$photo = other.getPhoto();
        if (this$photo == null ? other$photo != null : !this$photo.equals(other$photo)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof DogItem;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $breed = this.getBreed();
        result = result * PRIME + ($breed == null ? 43 : $breed.hashCode());
        final Object $breedType = this.getBreedType();
        result = result * PRIME + ($breedType == null ? 43 : $breedType.hashCode());
        final Object $origin = this.getOrigin();
        result = result * PRIME + ($origin == null ? 43 : $origin.hashCode());
        final Object $popularity = this.getPopularity();
        result = result * PRIME + ($popularity == null ? 43 : $popularity.hashCode());
        final Object $temperament = this.getTemperament();
        result = result * PRIME + ($temperament == null ? 43 : $temperament.hashCode());
        final Object $hypoallergenic = this.getHypoallergenic();
        result = result * PRIME + ($hypoallergenic == null ? 43 : $hypoallergenic.hashCode());
        result = result * PRIME + this.getIntelligence();
        final Object $photo = this.getPhoto();
        result = result * PRIME + ($photo == null ? 43 : $photo.hashCode());
        return result;
    }
}
