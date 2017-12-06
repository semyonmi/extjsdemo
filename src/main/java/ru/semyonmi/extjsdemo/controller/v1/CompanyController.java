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
import ru.semyonmi.extjsdemo.domain.Company;
import ru.semyonmi.extjsdemo.dto.CompanyDto;
import ru.semyonmi.extjsdemo.service.EdService;

@RestController
@RequestMapping("/api/v1/company")
public class CompanyController {

    @Autowired
    private EdService edService;
    @Autowired
    private ModelMapper modelMapper;

    @ApiOperation(value = "Get companies", notes = "Return companies")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @PostAuthorize("#oauth2.hasReadAccess()")
    public List<CompanyDto> get() {
        List<Company> companies = edService.getCompanies();
        return companies.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @ApiOperation(value = "Get company by id", notes = "Return company by id")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @PostAuthorize("#oauth2.hasReadAccess()")
    public CompanyDto getById(@PathVariable("id") Long id) {
        return convertToDto(edService.getCompany(Optional.ofNullable(id)));
    }

    @ApiOperation(value = "Create company", notes = "Create company")
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PostAuthorize("#oauth2.hasWriteAccess()")
    public CompanyDto save(@RequestBody CompanyDto company) {
        Company savedCompany = edService.saveCompany(Optional.ofNullable(convertToEntity(company)));
        return convertToDto(savedCompany);
    }

    @ApiOperation(value = "Update company", notes = "Update company")
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @PostAuthorize("#oauth2.hasWriteAccess()")
    public CompanyDto update(@RequestBody CompanyDto company) {
        Company savedCompany = edService.saveCompany(Optional.ofNullable(convertToEntity(company)));
        return convertToDto(savedCompany);
    }

    @ApiOperation(value = "Delete company", notes = "Delete company")
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @PostAuthorize("#oauth2.hasWriteAccess()")
    public void delete(@RequestParam List<Long> id) {
        edService.deleteCompanies(Optional.ofNullable(id));
    }

    @ApiOperation(value = "Delete company by id", notes = "Delete company by id")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @PostAuthorize("#oauth2.hasWriteAccess()")
    public void delete(@PathVariable Long id) {
        edService.deleteCompanies(Optional.ofNullable(Arrays.asList(id)));
    }

    private CompanyDto convertToDto(Company company) {
        return modelMapper.map(company, CompanyDto.class);
    }

    private Company convertToEntity(CompanyDto company) {
        return company == null ? null : modelMapper.map(company, Company.class);
    }
}
