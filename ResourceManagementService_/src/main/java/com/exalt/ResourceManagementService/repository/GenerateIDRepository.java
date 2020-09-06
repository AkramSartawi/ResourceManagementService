package com.exalt.ResourceManagementService.repository;

import com.exalt.ResourceManagementService.id.GenerateID;
import org.springframework.data.aerospike.repository.AerospikeRepository;

public interface GenerateIDRepository extends AerospikeRepository<GenerateID, Integer> {
}
