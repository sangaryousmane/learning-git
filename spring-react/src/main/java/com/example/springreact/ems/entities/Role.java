package com.example.springreact.ems.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "Role")
@Getter
@Setter
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public Role(String name){
        this.name=name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
//<div class="col-sm-8">
//<th:block th:each="role : ${listRoles}">
//<input type="checkbox" th:field="*{roles}"
//        th:text="${role.name}"
//        th:value="${role.id}"
//class="m-2"
//        />
//        - <small>[[${role.description}]]</small>
//<br/>
//</th:block>
//</div>