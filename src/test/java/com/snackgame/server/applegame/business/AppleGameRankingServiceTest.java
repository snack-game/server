package com.snackgame.server.applegame.business;

import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
import static com.snackgame.server.member.fixture.MemberFixture.땡칠2;
import static com.snackgame.server.member.fixture.MemberFixture.똥수;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManagerFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.transaction.TestTransaction;

import com.snackgame.server.annotation.ServiceTest;
import com.snackgame.server.applegame.business.domain.AppleGame;
import com.snackgame.server.applegame.business.domain.AppleGameSessionRepository;
import com.snackgame.server.applegame.business.domain.Coordinate;
import com.snackgame.server.applegame.business.domain.Range;
import com.snackgame.server.applegame.business.event.GameEndEvent;
import com.snackgame.server.applegame.fixture.TestFixture;
import com.snackgame.server.member.business.domain.Member;
import com.snackgame.server.member.fixture.MemberFixture;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ServiceTest
class AppleGameRankingServiceTest {

    @Autowired
    AppleGameSessionRepository appleGameSessions;
    @Autowired
    private AppleGameRankingService appleGameRankingService;

    @BeforeEach
    void setUp(
            @Autowired EntityManagerFactory entityManagerFactory,
            @Autowired ThreadPoolTaskExecutor taskExecutor
    ) throws InterruptedException {
        TestTransaction.end();
        MemberFixture.persistAllUsing(entityManagerFactory);

        appleGameRankingService.registerBestScoreWith(new GameEndEvent(
                playGame(땡칠(), new Range(
                        new Coordinate(0, 1),
                        new Coordinate(1, 3)
                ))
        ));
        appleGameRankingService.registerBestScoreWith(new GameEndEvent(
                playGame(땡칠(), new Range(
                        new Coordinate(0, 0),
                        new Coordinate(1, 0)
                ))
        ));
        appleGameRankingService.registerBestScoreWith(new GameEndEvent(
                playGame(똥수(), new Range(
                        new Coordinate(0, 0),
                        new Coordinate(1, 0)
                ))
        ));
        appleGameRankingService.registerBestScoreWith(new GameEndEvent(
                playGame(땡칠2())
        ));
        // 최고 점수 기록 함수는 각각의 쓰레드와 트랜잭션으로 실행될 수 있다.
        // 따라서 트랜잭션이 끝나기를 기다린 후에 그 결과를 평가해야 한다.
        taskExecutor.getThreadPoolExecutor().shutdown();
        taskExecutor.getThreadPoolExecutor().awaitTermination(5, TimeUnit.SECONDS);
    }

    @Test
    void 전체_랭킹을_가져온다() {
        assertThat(appleGameRankingService.rank50ByBestScore())
                .extracting("rank", "score")
                .containsExactly(
                        tuple(1L, 4),
                        tuple(2L, 2),
                        tuple(3L, 0)
                );
    }

    @Test
    void 자신의_최대_랭킹을_가져온다() {
        assertThat(appleGameRankingService.rankByBestScoreOf(땡칠().getId()))
                .extracting("score")
                .isEqualTo(4);
    }

    private AppleGame playGame(Member player, Range... ranges) {
        var game = appleGameSessions.save(new AppleGame(TestFixture.TWO_BY_FOUR(), player));
        for (Range range : ranges) {
            game.removeApplesIn(range);
        }
        game.end();
        return game;
    }
}
