package kb.wgwg.controller;

import kb.wgwg.common.ResponseMessage;
import kb.wgwg.common.StatusCode;
import kb.wgwg.domain.CoffeeChallenge;
import kb.wgwg.dto.BaseResponseDTO;
import kb.wgwg.dto.ChallengeDTO.*;
import kb.wgwg.service.CoffeeChallengeService;
import kb.wgwg.service.NChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

import static kb.wgwg.common.ResponseMessage.INTERNAL_SERVER_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/challenges")
public class ChallengeController {

    private final NChallengeService nChallengeService;
    private final CoffeeChallengeService coffeeChallengeService;

    @PostMapping(value = "/insert")
    public ResponseEntity<BaseResponseDTO> createChallenge(@RequestBody NChallengeInsertRequestDTO dto) {
        BaseResponseDTO<NChallengeInsertResponseDTO> response = new BaseResponseDTO<>();

        try {
            NChallengeInsertResponseDTO result = nChallengeService.insertNChallenge(dto);
            response.setMessage("성공적으로 챌린지가 생성되었습니다.");
            response.setStatus(200);
            response.setSuccess(true);
            response.setData(result);

            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            response.setMessage(e.getMessage());
            response.setStatus(404);
            response.setSuccess(false);

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(INTERNAL_SERVER_ERROR);
            response.setStatus(500);
            response.setSuccess(false);

            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping(value = "/participate")
    public ResponseEntity<BaseResponseDTO> participateNChallenge(@RequestBody NChallengeParticipateRequestDTO dto) {
        BaseResponseDTO<Void> response = new BaseResponseDTO<>();
        try {
            nChallengeService.participateNChallenge(dto);
            response.setMessage("성공적으로 챌린지에 참여하었습니다.");
            response.setStatus(200);
            response.setSuccess(true);

            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            response.setMessage(e.getMessage());
            response.setStatus(404);
            response.setSuccess(false);

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            response.setMessage("이미 참여중인 챌린지입니다.");
            response.setStatus(400);
            response.setSuccess(false);

            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.setMessage(INTERNAL_SERVER_ERROR);
            response.setStatus(500);
            response.setSuccess(false);

            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping(value = "/update/n")
    public ResponseEntity<BaseResponseDTO> updateNChallenge(@RequestBody NChallengeUpdateDTO dto) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            int updateRows = nChallengeService.updateNChallenge(dto);
            response.setStatus(StatusCode.OK);
            response.setMessage(ResponseMessage.CHALLENGE_UPDATE_SUCCESS);
            response.setSuccess(true);
            response.setData(updateRows);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e){
            response.setStatus(StatusCode.BAD_REQUEST);
            response.setMessage(ResponseMessage.NOT_FOUND_CHALLENGE);
            response.setSuccess(false);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e){
            response.setStatus(StatusCode.INTERNAL_SERVER_ERROR);
            response.setMessage(ResponseMessage.INTERNAL_SERVER_ERROR);
            response.setSuccess(false);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping(value = "/update/coffe")
    public ResponseEntity<BaseResponseDTO> updateCoffeeChallenge(@RequestBody CoffeeChallengeUpdateDTO dto) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            int updateRows = coffeeChallengeService.updateCoffeeChallenge(dto);
            response.setStatus(StatusCode.OK);
            response.setMessage(ResponseMessage.CHALLENGE_UPDATE_SUCCESS);
            response.setSuccess(true);
            response.setData(updateRows);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e){
            response.setStatus(StatusCode.BAD_REQUEST);
            response.setMessage(ResponseMessage.NOT_FOUND_CHALLENGE);
            response.setSuccess(false);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e){
            response.setStatus(StatusCode.INTERNAL_SERVER_ERROR);
            response.setMessage(ResponseMessage.INTERNAL_SERVER_ERROR);
            response.setSuccess(false);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping(value = "/read")
    public ResponseEntity<BaseResponseDTO> readNChallengeByStatus(@RequestBody NChallengeListRequestDTO requestDTO, @PageableDefault(size = 10) Pageable pageable) {
        BaseResponseDTO<Page<NChallengeListResponseDTO>> response = new BaseResponseDTO<>();
        try {
            Page<NChallengeListResponseDTO> result = nChallengeService.findNChallengeByStatus(requestDTO, pageable);
            response.setMessage(ResponseMessage.READ_CHALLENGELIST_SUCCESS);
            response.setStatus(StatusCode.OK);
            response.setSuccess(true);
            response.setData(result);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            response.setMessage(e.getMessage());
            response.setStatus(StatusCode.NOT_FOUND);
            response.setSuccess(false);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.setMessage(ResponseMessage.INTERNAL_SERVER_ERROR);
            response.setStatus(StatusCode.INTERNAL_SERVER_ERROR);
            response.setSuccess(false);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity deleteNChallenge(@PathVariable Long id) {
        BaseResponseDTO result = new BaseResponseDTO<>();
        nChallengeService.deleteNChallenge(id);
        result.setMessage("check");
        result.setStatus(200);
        result.setSuccess(true);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/read/{id}")
    public ResponseEntity<BaseResponseDTO> readNChallenge(@PathVariable Long id) {
        BaseResponseDTO<NChallengeReadResponseDTO> response = new BaseResponseDTO<>();

        try {
            NChallengeReadResponseDTO result = nChallengeService.findNChallengeById(id);
            response.setMessage("성공적으로 챌린지를 불러왔습니다.");
            response.setStatus(200);
            response.setSuccess(true);
            response.setData(result);
        } catch (EntityNotFoundException e) {
            response.setMessage(e.getMessage());
            response.setStatus(404);
            response.setSuccess(false);
        } catch (Exception e) {
            response.setMessage(INTERNAL_SERVER_ERROR);
            response.setStatus(500);
            response.setSuccess(false);
        }
        return ResponseEntity.ok(response);
    }
}
