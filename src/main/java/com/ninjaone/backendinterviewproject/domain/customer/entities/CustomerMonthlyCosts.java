//package com.ninjaone.backendinterviewproject.domain.customer.entities;
//
//import com.ninjaone.backendinterviewproject.application.dtos.CustomerMonthlyCostsDTO;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.math.BigDecimal;
//
//@Getter
//@Setter
//@Builder
//@EqualsAndHashCode
//@AllArgsConstructor
//public class CustomerMonthlyCosts {
//    private BigDecimal devicesCosts;
//    private BigDecimal antivirus;
//    private BigDecimal backup;
//    private BigDecimal screenShare;
//
//    public CustomerMonthlyCostsDTO toDTO() {
//        return CustomerMonthlyCostsDTO.builder()
//                .devicesCosts("Devices cost: $" + devicesCosts)
//                .antivirus("Antivirus cost: $" + antivirus)
//                .backup("Backup cost: $" + backup)
//                .screenShare("Screen Share: $" + screenShare)
//                .build();
//    }
//}
