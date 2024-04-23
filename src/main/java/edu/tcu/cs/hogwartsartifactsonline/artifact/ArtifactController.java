package edu.tcu.cs.hogwartsartifactsonline.artifact;

import edu.tcu.cs.hogwartsartifactsonline.artifact.converter.ArtifactDtoToArtifactConverter;
import edu.tcu.cs.hogwartsartifactsonline.artifact.converter.ArtifactToArtifactDtoConverter;
import edu.tcu.cs.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import edu.tcu.cs.hogwartsartifactsonline.system.Result;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Transactional
@RequestMapping("/api/v1/artifacts")
public class ArtifactController {
    private final ArtifactService artifactService;
    private final ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter;
    private final ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter;

    public ArtifactController(ArtifactService artifactService, ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter, ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter) {
        this.artifactService = artifactService;
        this.artifactToArtifactDtoConverter = artifactToArtifactDtoConverter;
        this.artifactDtoToArtifactConverter = artifactDtoToArtifactConverter;
    }

    @GetMapping("/{artifactId}")
    public Result findArtifactById(@PathVariable String artifactId){
        Artifact foundArtifact = this.artifactService.findById(artifactId);
        ArtifactDto artifactDto=this.artifactToArtifactDtoConverter.convert(foundArtifact);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", artifactDto);
    }
    @GetMapping
    public Result findAllArtifacts(){
        List<Artifact> foundArtifacts = this.artifactService.findAll();
        //Convert List of foundArtifacts to List of foundArtifactsDtos
        List<ArtifactDto> ArtifactDtos = foundArtifacts
                .stream()
                .map(this.artifactToArtifactDtoConverter::convert).collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find All Success", ArtifactDtos);
    }

    @PostMapping
    public Result addArtifact(@Valid @RequestBody ArtifactDto artifactDto){
        //Convert ArtifactDto to Artifact
        Artifact newArtifact = this.artifactDtoToArtifactConverter.convert(artifactDto);
        Artifact savedArtifact = this.artifactService.save(newArtifact);
        ArtifactDto savedArtifactDto = this.artifactToArtifactDtoConverter.convert(savedArtifact);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedArtifactDto);
    }

    @PutMapping("/{artifactId}")
    public Result updateArtifact(@PathVariable String artifactId, @Valid @RequestBody ArtifactDto artifactDto) {
        Artifact update = this.artifactDtoToArtifactConverter.convert(artifactDto);
        Artifact updatedArtifact = this.artifactService.update(artifactId, update);
        ArtifactDto updatedArtifactDto = this.artifactToArtifactDtoConverter.convert(updatedArtifact);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedArtifactDto);
    }

    @DeleteMapping("/{artifactId}")
    public Result deleteArtifact(@PathVariable String artifactId) {
        this.artifactService.delete(artifactId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
