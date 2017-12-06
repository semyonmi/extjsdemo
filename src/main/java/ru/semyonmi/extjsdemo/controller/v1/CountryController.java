package ru.semyonmi.extjsdemo.controller.v1;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.swagger.annotations.ApiOperation;
import ru.semyonmi.extjsdemo.domain.Country;
import ru.semyonmi.extjsdemo.dto.CountryDto;
import ru.semyonmi.extjsdemo.service.EdService;

@RestController
@RequestMapping("/api/v1/country")
public class CountryController {

    @Autowired
    private EdService edService;
    @Autowired
    private ModelMapper modelMapper;

    @ApiOperation(value = "Get countries", notes = "Return countries")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @PostAuthorize("#oauth2.hasReadAccess()")
    public List<CountryDto> get() {
        List<Country> countries = edService.getCountries();
        return countries.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @ApiOperation(value = "Get country by id", notes = "Return country by id")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @PostAuthorize("#oauth2.hasReadAccess()")
    public CountryDto getById(@PathVariable("id") Long id) {
        return convertToDto(edService.getCountry(Optional.ofNullable(id)));
    }

    @ApiOperation(value = "Create country", notes = "Create country")
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostAuthorize("#oauth2.hasWriteAccess()")
    public CountryDto save(@RequestBody CountryDto country) {
        Country savedCountry = edService.saveCountry(Optional.ofNullable(convertToEntity(country)));
        return convertToDto(savedCountry);
    }

    @ApiOperation(value = "Update country", notes = "Update country")
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @PostAuthorize("#oauth2.hasWriteAccess()")
    public CountryDto update(@RequestBody CountryDto country) {
        Country savedCountry = edService.saveCountry(Optional.ofNullable(convertToEntity(country)));
        return convertToDto(savedCountry);
    }

    @ApiOperation(value = "Delete country", notes = "Delete country")
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @PostAuthorize("#oauth2.hasWriteAccess()")
    public void delete(@RequestParam List<Long> id) {
        edService.deleteCountries(Optional.ofNullable(id));
    }

    @ApiOperation(value = "Delete country by id", notes = "Delete country by id")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @PostAuthorize("#oauth2.hasWriteAccess()")
    public void delete(@PathVariable Long id) {
        edService.deleteCountries(Optional.ofNullable(Arrays.asList(id)));
    }

    private CountryDto convertToDto(Country country) {
        return modelMapper.map(country, CountryDto.class);
    }

    private Country convertToEntity(CountryDto country) {
        return country == null ? null : modelMapper.map(country, Country.class);
    }
}
