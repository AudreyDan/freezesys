package com.sery.freezesys.service.impl;

import com.sery.freezesys.dao.NitMapper;
import com.sery.freezesys.dao.PatientMapper;
import com.sery.freezesys.dao.StrawMapper;
import com.sery.freezesys.model.Divepipe;
import com.sery.freezesys.model.Patient;
import com.sery.freezesys.model.Straw;
import com.sery.freezesys.model.StrawDTO;
import com.sery.freezesys.service.StrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StrawServiceImpl implements StrawService {
    @Autowired
    private StrawMapper strawMapper;
    @Autowired
    private PatientMapper patientMapper;
    @Autowired
    private NitMapper nitMapper;

    @Override
    public List<StrawDTO> getStrawList() {
        List<StrawDTO> strawList = strawMapper.selectAllStraw();
        return strawList;
    }

    @Override
    public int addStraw(StrawDTO strawDTO) {
        //根据病历号，女方姓名获取病人Id
        Map patientMap = new HashMap();
        patientMap.put("medicalRecord",strawDTO.getMedicalRecord());
        patientMap.put("femaleName",strawDTO.getFemaleName());
        Patient patient = patientMapper.selectPatient(patientMap);
        int patientId;
        //根据病历号，姓名获取病人Id，如果存在，直接把病人Id插入麦管记录
        if (patient != null){
            patientId = patient.getPatientId();
        }else {//否则新增一条病人记录，并返回病人Id
            Patient patientAdd = new Patient();
            patientAdd.setMedicalRecord(strawDTO.getMedicalRecord());
            patientAdd.setFemaleName(strawDTO.getFemaleName());
            patientAdd.setMaleName(strawDTO.getMaleName());
            patientMapper.insertPatient(patientAdd);
            patientId = patientAdd.getPatientId();
        }
        //根据液氮罐编号，吊桶编号，套管编号获取套管ID
        Map nitMap = new HashMap();
        nitMap.put("nitNum",strawDTO.getNitNum());
        nitMap.put("tubNum",strawDTO.getTubNum());
        nitMap.put("divepipeNum",strawDTO.getDivepipeNum());
        Divepipe divepipe = nitMapper.selectDivepipeId(nitMap);
        Straw straw = new Straw();
        straw.setStrawNum(strawDTO.getStrawNum());
        straw.setFreezeNum(strawDTO.getFreezeNum());
        straw.setBarcodeNum(strawDTO.getBarcodeNum());
        straw.setPatientId(patientId);
        straw.setDivepipeId(divepipe.getDivepipeId());
        straw.setSampleType(strawDTO.getSampleType());
        straw.setSampleAmount(strawDTO.getSampleAmount());
        straw.setFreezeTime(strawDTO.getFreezeTime());
        straw.setExpireTime(strawDTO.getExpireTime());
        straw.setFreezeStatus(strawDTO.getFreezeStatus());
        straw.setOperator(strawDTO.getOperator());
        straw.setRemark(strawDTO.getRemark());
        int result = strawMapper.insertStraw(straw);
        return result;
    }
}
