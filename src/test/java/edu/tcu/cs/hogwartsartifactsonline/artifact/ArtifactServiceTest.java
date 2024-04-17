package edu.tcu.cs.hogwartsartifactsonline.artifact;

import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {
    @Mock
    ArtifactRepository artifactRepository;
    @InjectMocks
    ArtifactService artifactService;
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        //Given. Arrange inputs and targets. Define the behavior of Mock object artifactRepository
        /*
        "id": "1250808601744904192",
        "name": "Invisibility Cloak",
        "description": "An invisibility cloak is used to make the wearer invisible.",
        "imageUrl": "ImageUrl",*/
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDesc("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Wizard w = new Wizard();
        w.setId(2);
        w.setName("Harry Potter");

        a.setOwner(w);
        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a));// Defines the behavior of the mock object

        //When. Act on the target behaviour. When should cover the method to be tested.
        Artifact returnedArtifact = artifactService.findById("1250808601744904192");

        //Then. Assert expected outcomes.
        assertThat(returnedArtifact.getId()).isEqualTo(a.getId());
        assertThat(returnedArtifact.getName()).isEqualTo(a.getName());
        assertThat(returnedArtifact.getDesc()).isEqualTo(a.getDesc());
        assertThat(returnedArtifact.getImageUrl()).isEqualTo(a.getImageUrl());
        verify(artifactRepository,times(1)).findById("1250808601744904192");
    }

    @Test
    void testFindByIdNotFound() {
        //Given
        given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());

        //When
        Throwable thrown = catchThrowable(()->{
            Artifact returnedArtifact = artifactService.findById("1250808601744904192");
        });

        //Then
        assertThat(thrown)
                .isInstanceOf(ArtifactNotFoundException.class)
                .hasMessage("Could not find artifact with Id 1250808601744904192 :(");
        verify(artifactRepository,times(1)).findById("1250808601744904192");
    }
}