package ir.netpick.mailmine.scrape.service;

import java.util.List;
import java.util.UUID;

import ir.netpick.mailmine.common.exception.ResourceNotFoundException;
import ir.netpick.mailmine.scrape.model.Pipeline;
import ir.netpick.mailmine.scrape.repository.PipelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PipelineService {

    private final PipelineRepository pipelineRepository;

    public List<Pipeline> getAllPipelines() {
        return pipelineRepository.findAll();
    }

    public Pipeline getPipeline(UUID pipelineId) {
        return  pipelineRepository.findById(pipelineId)
                .orElseThrow( () -> new ResourceNotFoundException("Pipeline with id %s was not found".formatted(pipelineId)));
    }

    public Pipeline createPipeline(Pipeline pipeline) {
        return pipelineRepository.save(pipeline);
    }

    public void updatePipeline(Pipeline pipeline) {
        pipelineRepository.save(pipeline);
    }

    public void deletePipeline(Pipeline pipeline) {
        pipelineRepository.delete(pipeline);
    }

}
