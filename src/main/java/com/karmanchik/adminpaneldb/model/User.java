package com.karmanchik.adminpaneldb.model;

import com.karmanchik.adminpaneldb.bot.Role;
import com.karmanchik.adminpaneldb.bot.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "chat_id", name = "users_unique_chatid_idx")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractBaseEntity {
    @Column(name = "chat_id", unique = true, nullable = false)
    @NotNull
    private Integer chatId;
    @Column(name = "name", unique = true, nullable = false)
    @NotBlank
    private String name;
    @Column(name = "bot_state", nullable = false)
    @NotBlank
    private State botState;
    @Column(name = "group_id")
    private Integer groupId;
    @Column(name = "role_name", nullable = false)
    @NotBlank
    private String role_name;

    public User(int chatId) {
        this.chatId = chatId;
        this.name = String.valueOf(chatId);
        this.botState = State.START;
        this.role_name = Role.NONE.toString();
        this.groupId = 0;
    }
}
