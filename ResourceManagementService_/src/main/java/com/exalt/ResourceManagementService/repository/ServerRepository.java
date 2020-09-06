package com.exalt.ResourceManagementService.repository;

import com.exalt.ResourceManagementService.model.Server;
import org.springframework.data.aerospike.repository.AerospikeRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerRepository extends  AerospikeRepository<Server, Integer> {
}
