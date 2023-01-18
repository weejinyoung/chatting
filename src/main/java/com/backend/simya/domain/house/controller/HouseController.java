package com.backend.simya.domain.house.controller;

import com.backend.simya.domain.house.dto.request.HouseOpenRequestDto;
import com.backend.simya.domain.house.dto.request.HouseRequestDto;
import com.backend.simya.domain.house.dto.request.HouseUpdateRequestDto;
import com.backend.simya.domain.house.dto.response.HouseIntroductionResponseDto;
import com.backend.simya.domain.house.dto.response.HouseResponseDto;
import com.backend.simya.domain.house.dto.response.HouseShowResponseDto;
import com.backend.simya.domain.house.entity.Category;
import com.backend.simya.domain.house.entity.House;
import com.backend.simya.domain.house.service.HouseService;
import com.backend.simya.domain.user.entity.User;
import com.backend.simya.domain.user.service.UserService;
import com.backend.simya.global.common.BaseException;
import com.backend.simya.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/simya/house")
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;
    private final UserService userService;

    @PostMapping("")
    public BaseResponse<HouseResponseDto> createHouseRoom(@RequestBody HouseRequestDto houseRequestDto) {
        try {
            return new BaseResponse(houseService.createHouse(houseRequestDto));
        } catch (BaseException e) {
            return new BaseResponse(e.getStatus());
        }
    }

    @PatchMapping("{houseId}")
    public BaseResponse<String> openHouse(@PathVariable("houseId") Long houseId, @RequestBody HouseOpenRequestDto houseOpenRequestDto) {
        try {
            User loginUser = userService.getMyUserWithAuthorities();
            houseService.openHouse(loginUser, houseOpenRequestDto);
            return new BaseResponse<>("이야기 집이 오픈되었습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("{houseId}")
    public BaseResponse<HouseIntroductionResponseDto> showHouse(@PathVariable("houseId") Long houseId) {
        try {
            return new BaseResponse<>(houseService.showHouse(houseId));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/main/{houseId}")
    public BaseResponse<String> updateMain(@PathVariable("houseId") Long houseId, @RequestBody HouseUpdateRequestDto houseUpdateRequestDto) {
        try {
            User loginUser = userService.getMyUserWithAuthorities();
            houseService.updateMain(loginUser, houseUpdateRequestDto);
            return new BaseResponse<>("이야기 집 간판이 수정되었습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/close/{houseId}")
    public BaseResponse<String> closeHouseRoom(@PathVariable("houseId") Long houseId) {
        try {
            User loginUser = userService.getMyUserWithAuthorities();
            houseService.closeHouseRoom(loginUser, houseId);
            return new BaseResponse<>("이야기 집이 폐점되었습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/menu/{houseId}")
    public BaseResponse<String> updateMainMenu(@PathVariable("houseId") Long houseId, @RequestParam("menu") String menu) {

        try {
            User loginUser = userService.getMyUserWithAuthorities();
            House house = houseService.getHouse(houseId);

            if(house.getCategory().equals(Category.nameOf(menu))) {
                return new BaseResponse<>("해당 메뉴는 이미 대표 메뉴입니다.");
            }
            houseService.updateMainMenu(houseId, loginUser, menu);
            return new BaseResponse<>("이야기 집의 메인 메뉴가 바뀌었습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


}
