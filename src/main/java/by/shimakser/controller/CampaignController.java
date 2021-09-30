package by.shimakser.controller;

import by.shimakser.dto.CampaignDto;
import by.shimakser.filter.FilterRequest;
import by.shimakser.mapper.CampaignMapper;
import by.shimakser.model.Campaign;
import by.shimakser.service.CampaignFilterService;
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

@RestController
@RequestMapping("/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    private final CampaignMapper campaignMapper;

    private final CampaignFilterService campaignFilterService;

    @Autowired
    public CampaignController(CampaignService campaignService, CampaignMapper campaignMapper, CampaignFilterService campaignFilterService) {
        this.campaignService = campaignService;
        this.campaignMapper = campaignMapper;
        this.campaignFilterService = campaignFilterService;
    }

    @PostMapping
    public ResponseEntity<CampaignDto> addCampaign(@RequestBody CampaignDto campaignDto) throws AlreadyBoundException {
        Campaign newCampaign = campaignMapper.mapToEntity(campaignDto);
        campaignService.add(newCampaign);
        return new ResponseEntity<>(campaignDto, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public CampaignDto getCampaignById(@PathVariable Long id) throws NotFoundException {
        return campaignMapper.mapToDto(campaignService.get(id));
    }

    @PostMapping("/filter")
    public List<CampaignDto> getByFilter(@RequestBody FilterRequest filterRequest) {
        return campaignMapper.mapToListDto(campaignFilterService.getByFilter(filterRequest));
    }

    @PutMapping(value = "/{id}")
    public CampaignDto updateCampaignById(@PathVariable("id") Long id,
                                          @RequestBody CampaignDto newCampaignDto,
                                          Principal creator) throws NotFoundException {
        Campaign campaign = campaignMapper.mapToEntity(newCampaignDto);
        campaignService.update(id, campaign, creator);
        return newCampaignDto;
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteCampaignById(@PathVariable("id") Long id, Principal creator)
            throws NotFoundException {
        campaignService.delete(id, creator);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/deleted/{id}")
    public CampaignDto getDeletedCampaignById(@PathVariable Long id) throws NotFoundException {
        return campaignMapper.mapToDto(campaignService.getDeletedCampaign(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/deleted")
    public List<CampaignDto> getAllDeletedCampaigns() {
        return campaignMapper.mapToListDto(campaignService.getDeletedCampaigns());
    }
}
