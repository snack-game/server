package com.snackgame.server.rank.applegame;

import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
import static com.snackgame.server.member.fixture.MemberFixture.정환;
import static com.snackgame.server.member.fixture.MemberFixture.똥수;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.snackgame.server.applegame.domain.Coordinate;
import com.snackgame.server.applegame.domain.Range;
import com.snackgame.server.applegame.domain.game.AppleGame;
import com.snackgame.server.applegame.domain.game.AppleGames;
import com.snackgame.server.applegame.event.GameEndEvent;
import com.snackgame.server.applegame.fixture.TestFixture;
import com.snackgame.server.member.MemberService;
import com.snackgame.server.member.domain.Member;
import com.snackgame.server.member.fixture.MemberFixture;
import com.snackgame.server.rank.applegame.domain.BestScores;
import com.snackgame.server.support.general.ServiceTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ServiceTest
class BestScoreRankingServiceTest {

    @Autowired
    AppleGames appleGames;
    @Autowired
    private BestScoreRankingService bestScoreRankingService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BestScores bestScores;

    @BeforeEach
    void setUp(@Autowired ThreadPoolTaskExecutor taskExecutor) throws InterruptedException {
        MemberFixture.saveAll();

        bestScoreRankingService.renewBestScoreWith(new GameEndEvent(
                playGame(땡칠().getId(), new Range(
                        new Coordinate(0, 1),
                        new Coordinate(1, 3)
                ))
        ));
        bestScoreRankingService.renewBestScoreWith(new GameEndEvent(
                playGame(땡칠().getId(), new Range(
                        new Coordinate(0, 0),
                        new Coordinate(1, 0)
                ))
        ));
        bestScoreRankingService.renewBestScoreWith(new GameEndEvent(
                playGame(똥수().getId(), new Range(
                        new Coordinate(0, 0),
                        new Coordinate(1, 0)
                ))
        ));
        bestScoreRankingService.renewBestScoreWith(new GameEndEvent(
                playGame(정환().getId())
        ));
        // 최고 점수 기록 함수는 각각의 쓰레드와 트랜잭션으로 실행될 수 있다.
        // 따라서 트랜잭션이 끝나기를 기다린 후에 그 결과를 평가해야 한다.
        taskExecutor.getThreadPoolExecutor().shutdown();
        taskExecutor.getThreadPoolExecutor().awaitTermination(5, TimeUnit.SECONDS);
    }

    @Test
    void 전체_랭킹을_가져온다() {
        assertThat(bestScoreRankingService.rankLeadingBestScores())
                .extracting("rank", "score")
                .containsExactly(
                        tuple(1L, 4),
                        tuple(2L, 2),
                        tuple(3L, 0)
                );
    }

    @Test
    void 자신의_최대_랭킹을_가져온다() {
        assertThat(bestScoreRankingService.rankBestScoreOf(땡칠().getId()))
                .extracting("score")
                .isEqualTo(4);
    }

    @Test
    void 전체에서_자신의_최고점수를_랭크한다() {
        assertThat(bestScoreRankingService.rankBestScoreOf(똥수().getId()))
                .extracting("rank")
                .isEqualTo(2L);
    }

    @Test
    void 게스트_점수는_랭크에_기록되지_않는다() {
        Member guest = memberService.createGuest();
        bestScoreRankingService.renewBestScoreWith(new GameEndEvent(
                playGame(guest.getId(), new Range(
                        new Coordinate(0, 0),
                        new Coordinate(1, 0)
                ))
        ));

        assertThat(bestScores.findByOwnerId(guest.getId())).isEmpty();
    }

    private AppleGame playGame(Long playerId, Range... ranges) {
        var game = appleGames.save(new AppleGame(TestFixture.TWO_BY_FOUR(), playerId));
        for (Range range : ranges) {
            game.removeApplesIn(range);
        }
        game.finish();
        return game;
    }
}
