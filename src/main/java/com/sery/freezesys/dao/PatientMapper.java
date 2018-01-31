package com.sery.freezesys.dao;

import com.sery.freezesys.model.Patient;

import java.util.List;

public interface PatientMapper {
    /**
     * 插入一条病人信息
     * @param patient
     * @return
     */
    int insertPatient(Patient patient);

    /**
     * 查询所有的病人信息
     * @return
     */
    List<Patient> selectAllPatient();

    /**
     * 更新病人信息
     * @param patient
     * @return
     */
    int updatePatient(Patient patient);
}