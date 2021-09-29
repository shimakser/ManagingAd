package by.shimakser.controller;

import by.shimakser.dto.CampaignDto;
import by.shimakser.filter.CampaignFilterService;
import by.shimakser.filter.Filter;
import by.shimakser.filter.FilterRequest;
import by.shimakser.mapper.CampaignMapper;
import by.shimakser.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class CampaignFilterController {

    private final CampaignFilterService campaignFilterService;

    private final CampaignMapper campaignMapper;

    private final CampaignRepository campaignRepository;

    @Autowired
    public CampaignFilterController(CampaignFilterService campaignFilterService, CampaignMapper campaignMapper, CampaignRepository campaignRepository) {
        this.campaignFilterService = campaignFilterService;
        this.campaignMapper = campaignMapper;
        this.campaignRepository = campaignRepository;
    }

    @PostMapping(value = "/campaigns/filter")
    public List<CampaignDto> getByFirst(@RequestBody Filter filter,
                                        @RequestParam Optional<Integer> page,
                                        @RequestParam Optional<Integer> size,
                                        @RequestParam Optional<String> sortBy) {
        FilterRequest filterRequest = new FilterRequest(filter,
                page.orElse(1),
                size.orElse(campaignRepository.findAll().size()),
                sortBy.orElse("id"));

        return campaignFilterService.getByFilter(filterRequest).stream()
                .map(campaignMapper::mapToDto).collect(Collectors.toList());
    }
}
