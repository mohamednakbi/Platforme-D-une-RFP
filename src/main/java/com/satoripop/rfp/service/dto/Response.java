package com.satoripop.rfp.service.dto;

import java.util.HashMap;
import java.util.List;

public class Response {

    private HashMap<CVDTO, Double> cvMap;
    private HashMap<TechnologyDTO, Double> technologyMap;

    private List<ReferenceDTO> references;
    private List<ContextDTO> contexts;

    public Response() {}

    public Response(
        HashMap<CVDTO, Double> CV,
        HashMap<TechnologyDTO, Double> Technologies,
        List<ReferenceDTO> references,
        List<ContextDTO> contexts
    ) {
        this.cvMap = CV;
        this.technologyMap = Technologies;
        this.references = references;
        this.contexts = contexts;
    }

    public HashMap<CVDTO, Double> getCvMap() {
        return cvMap;
    }

    public void setCvMap(HashMap<CVDTO, Double> cvMap) {
        this.cvMap = cvMap;
    }

    public HashMap<TechnologyDTO, Double> getTechnologyMap() {
        return technologyMap;
    }

    public void setTechnologyMap(HashMap<TechnologyDTO, Double> technologyMap) {
        this.technologyMap = technologyMap;
    }

    public List<ReferenceDTO> getReferences() {
        return references;
    }

    public void setReferences(List<ReferenceDTO> references) {
        this.references = references;
    }

    public List<ContextDTO> getContexts() {
        return contexts;
    }

    public void setContexts(List<ContextDTO> contexts) {
        this.contexts = contexts;
    }

    @Override
    public String toString() {
        return (
            "Response{" + "CV=" + cvMap + ", Technologies=" + technologyMap + ", references=" + references + ", contexts=" + contexts + '}'
        );
    }
}
