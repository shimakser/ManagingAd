package by.shimakser.controller;

import by.shimakser.dto.CampaignDto;
import by.shimakser.mapper.CampaignMapper;
import by.shimakser.model.Campaign;
import by.shimakser.service.CampaignService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.rmi.AlreadyBoundException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class CampaignController {

    private final CampaignService campaignService;

    private final CampaignMapper campaignMapper;

    @Autowired
    public CampaignController(CampaignService campaignService, CampaignMapper campaignMapper) {
        this.campaignService = campaignService;
        this.campaignMapper = campaignMapper;
    }

    @PostMapping(value = "/campaigns")
    public ResponseEntity<CampaignDto> addCampaign(@RequestBody CampaignDto campaignDto) throws AlreadyBoundException {
        Campaign newCampaign = campaignMapper.mapToEntity(campaignDto);
        campaignService.add(newCampaign);
        return new ResponseEntity<>(campaignDto, HttpStatus.CREATED);
    }

    @GetMapping(value = "/campaigns/{id}")
    public List<CampaignDto> getCampaignById(@PathVariable Long id) throws NotFoundException {
        return campaignService.get(id)
                .stream().map(campaignMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/campaigns")
    public List<CampaignDto> getAllCampaigns(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<String> sortBy
    ) {
        return campaignService.getAll(page, size, sortBy)
                .stream().map(campaignMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @PutMapping(value = "/campaigns/{id}")
    public CampaignDto updateCampaignById(@PathVariable("id") Long id,
                                          @RequestBody CampaignDto newCampaignDto,
                                          Principal creator) throws NotFoundException {
        Campaign campaign = campaignMapper.mapToEntity(newCampaignDto);
        campaignService.update(id, campaign, creator);
        return newCampaignDto;
    }

    @DeleteMapping(value = "/campaigns/{id}")
    public ResponseEntity<HttpStatus> deleteCampaignById(@PathVariable("id") Long id, Principal creator)
            throws NotFoundException {
        campaignService.delete(id, creator);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/campaigns/deleted/{id}")
    public CampaignDto getDeletedCampaignById(@PathVariable Long id) throws NotFoundException {
        return campaignMapper.mapToDto(campaignService.getDeletedCampaign(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/campaigns/deleted")
    public List<CampaignDto> getAllDeletedCampaigns() {
        return campaignService.getDeletedCampaigns()
                .stream().map(campaignMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
