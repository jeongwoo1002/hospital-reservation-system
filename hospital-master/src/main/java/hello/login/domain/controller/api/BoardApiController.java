package hello.login.domain.controller.api;

import hello.login.domain.config.auth.PrincipalDetail;
import hello.login.domain.dto.BoardForm;
import hello.login.domain.dto.ReplyDto;
import hello.login.domain.dto.ResponseDto;
import hello.login.domain.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    /**
     * 글쓰기
     */
    @PostMapping("/api/board")
    public ResponseDto<Integer> write(@RequestBody BoardForm boardForm,
                                      @AuthenticationPrincipal PrincipalDetail principal) {

        boardService.write(boardForm, principal.getUser());
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    /**
     * 글삭제
     */
    @DeleteMapping("/api/board/{id}")
    public ResponseDto<Integer> delete(@PathVariable Long id) {

        boardService.delete(id);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    /**
     * 글수정
     */
    @PutMapping("/api/board/{id}")
    public ResponseDto<Integer> update(@PathVariable Long id, @RequestBody BoardForm boardForm) {
        boardService.update(id, boardForm);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    /**
     * 댓글쓰기
     */
    @PostMapping("/api/board/{boardId}/reply")
    public ResponseDto<Integer> replyWrite(@RequestBody ReplyDto replyDto) {

        boardService.replyWrite(replyDto);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/api/board/{boardId}/reply/{replyId}")
    public ResponseDto<Integer> replyDelete(@PathVariable Long replyId) {
        boardService.replyDelete(replyId);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }
}
