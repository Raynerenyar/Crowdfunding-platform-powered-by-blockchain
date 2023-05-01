//SPDX-License-Identifier: GPL-3.0

pragma solidity ^0.8.0;

import "./Crowdfunding.sol";

contract CrowdfundingFactory is Ownable {
    mapping(address => ProjectDetails) public projects;
    mapping(address => bool) public allowedAddresses;
    bool isFreeForAll;

    struct ProjectDetails {
        string title;
        Crowdfunding project;
    }

    event CreateNewProjectEvent(
        address _projectAddress,
        address _projectCreator,
        string _title,
        uint _goal,
        uint _deadline,
        address _tokenUsed
    );
    event ContributeToProjectEvent(
        address _projectAddress,
        address _contributor,
        address _tokenAddress,
        uint _amount
    );
    event GetRefundFromProjectEvent(
        address _projectAddress,
        address _contributor,
        address _RefundToken,
        uint _RefundAmount
    );
    event CreateRequestForProjectEvent(
        address _projectAddress,
        address _projectCreator,
        string _title,
        address _recipient,
        uint _amount
    );
    event ReceiveContributionFromProjectEvent(
        address _projectAddress,
        address _projectCreator,
        address _recipient,
        uint _amount,
        uint _requestNo
    );
    event voteRequestForProjectEvent(
        address _projectAddress,
        address _voter,
        uint _requestNo,
        uint _valueOfVote
    );

    constructor() {
        isFreeForAll = true;
    }

    function createNewProject(
        uint _goal,
        uint _deadline,
        address _acceptingThisToken,
        string memory _title
    ) public {
        if (!isFreeForAll) {
            require(
                allowedAddresses[msg.sender] == true,
                "Address is not allowed"
            );
        }

        Crowdfunding crowdfunding = new Crowdfunding(
            address(this),
            _goal,
            _deadline,
            _acceptingThisToken
        );

        require(address(crowdfunding) != address(0));
        crowdfunding.transferOwnership(msg.sender);

        ProjectDetails storage newProject = projects[address(crowdfunding)];

        newProject.title = _title;
        newProject.project = crowdfunding;

        emit CreateNewProjectEvent(
            address(crowdfunding),
            msg.sender,
            _title,
            _goal,
            _deadline,
            _acceptingThisToken
        );
    }

    function contributeToProject(address _projectAddress, uint _amount) public {
        Crowdfunding project = Crowdfunding(_projectAddress);
        address _contributor;
        address _tokenAddress = project.tokenAddress();

        (_contributor) = project.contribute(_amount, msg.sender);
        emit ContributeToProjectEvent(
            _projectAddress,
            _contributor,
            _tokenAddress,
            _amount
        );
    }

    function getBalanceOfProject(
        address _projectAddress
    ) public view returns (uint) {
        Crowdfunding project = Crowdfunding(_projectAddress);
        return project.getBalance();
    }

    function getRefundFromProject(address _projectAddress) public {
        Crowdfunding project = Crowdfunding(_projectAddress);
        uint _RefundAmount = project.getRefund(msg.sender);
        emit GetRefundFromProjectEvent(
            _projectAddress,
            msg.sender,
            project.tokenAddress(),
            _RefundAmount
        );
    }

    function createRequestForProject(
        address _projectAddress,
        string memory _title,
        address _recipient,
        uint _amount
    ) public {
        Crowdfunding project = Crowdfunding(_projectAddress);
        project.createRequest(_title, _recipient, _amount);

        emit CreateRequestForProjectEvent(
            _projectAddress,
            msg.sender,
            _title,
            _recipient,
            _amount
        );
    }

    function voteRequestForProject(
        address _projectAddress,
        uint _requestNo
    ) public {
        Crowdfunding project = Crowdfunding(_projectAddress);
        uint valueOfVote = project.voteRequest(_requestNo, msg.sender);

        emit voteRequestForProjectEvent(
            _projectAddress,
            msg.sender,
            _requestNo,
            valueOfVote
        );
    }

    function receiveContributionFromProject(
        address _projectAddress,
        uint _requestNo
    ) public {
        Crowdfunding project = Crowdfunding(_projectAddress);
        address _recipient;
        uint _amount;
        (_recipient, _amount) = project.receiveContribution(_requestNo);

        emit ReceiveContributionFromProjectEvent(
            _projectAddress,
            msg.sender,
            _recipient,
            _amount,
            _requestNo
        );
    }

    function allowAddress(address _allowThisAddress) public onlyOwner {
        require(allowedAddresses[_allowThisAddress] == false);
        allowedAddresses[_allowThisAddress] = true;
    }

    function disallowAddress(address _disallowThisAddress) public onlyOwner {
        require(allowedAddresses[_disallowThisAddress] == true);
        allowedAddresses[_disallowThisAddress] = false;
    }

    function getRequestOfProject(
        address _projectAddress,
        uint _requestNo
    ) public view returns (string memory, address, uint, bool, uint, uint) {
        Crowdfunding project = Crowdfunding(_projectAddress);
        return (
            project.getRequestTitle(_requestNo),
            project.getRequestRecipient(_requestNo),
            project.getRequestAmount(_requestNo),
            project.getRequestCompleted(_requestNo),
            project.getRequestNumOfVoters(_requestNo),
            project.getRequestValueOfVotes(_requestNo)
        );
    }

    function getNumOfRequestOfProject(
        address _projectAddress
    ) public view returns (uint) {
        Crowdfunding project = Crowdfunding(_projectAddress);
        return project.numRequests();
    }

    function getTitleOfProject(
        address _projectAddress
    ) public view returns (string memory, address) {
        string memory d = projects[_projectAddress].title;
        Crowdfunding a = projects[_projectAddress].project;
        return (d, address(a));
    }

    function toggleFreeForAll() public {
        isFreeForAll = !isFreeForAll;
    }

    function approveTokenForProject(
        address _projectAddress,
        uint _amount
    ) public {
        Crowdfunding project = Crowdfunding(_projectAddress);
        IERC20 token = IERC20(project.tokenAddress());
        token.approve(_projectAddress, _amount);
    }
}
