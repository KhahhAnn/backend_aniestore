package com.khahhann.backend.service.impl;

import com.khahhann.backend.model.Category;
import com.khahhann.backend.model.Discount;
import com.khahhann.backend.repository.DiscountRepository;
import com.khahhann.backend.response.ApiResponse;
import com.khahhann.backend.service.DiscountService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class DiscountServiceImpl implements DiscountService {
	private DiscountRepository discountRepository;
	@Override
	public Discount updateCategory(Discount discount) {
		Discount existDiscount = this.discountRepository.getReferenceById(discount.getId());
		if(existDiscount == null) {
			return null;
		}
		existDiscount.setDiscountName(discount.getDiscountName());
		existDiscount.setPercentDiscount(discount.getPercentDiscount());
		existDiscount.setApplyDate(discount.getApplyDate());
		existDiscount.setExpiry(discount.getExpiry());
		return this.discountRepository.saveAndFlush(existDiscount);
	}

	@Override
	public ApiResponse addDiscount(Discount discount) {
		ApiResponse apiResponse = new ApiResponse();
		if(this.discountRepository.findByDiscountName(discount.getDiscountName()) != null) {
			apiResponse.setStatus(false);
			apiResponse.setMessage("Thêm thất bại");
			return apiResponse;
		}
		this.discountRepository.saveAndFlush(discount);
		apiResponse.setStatus(true);
		apiResponse.setMessage("Thêm thành công");
		return apiResponse;	}

	@Override
	public ApiResponse deleteDiscount(UUID id) {
		ApiResponse apiResponse = new ApiResponse();
		Discount discount = this.discountRepository.getReferenceById(id);
		if(!this.discountRepository.existsById(id)) {
			apiResponse.setStatus(false);
			apiResponse.setMessage("Xóa thất bại");
			return apiResponse;
		}
		this.discountRepository.deleteById(id);
		apiResponse.setStatus(true);
		apiResponse.setMessage("Xóa thành công");
		return apiResponse;
	}

	@Override
	public Discount getDiscountByName(String discountName) {
		return this.discountRepository.findByDiscountName(discountName);
	}
}
